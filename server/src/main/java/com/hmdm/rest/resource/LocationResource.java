/*
 *
 * Headwind MDM: Open Source Android MDM Software
 * https://h-mdm.com
 *
 * Copyright (C) 2019 Headwind Solutions LLC (http://h-sms.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.hmdm.rest.resource;

import com.hmdm.persistence.LocationDAO;
import com.hmdm.persistence.UserDAO;
import com.hmdm.persistence.domain.Group;
import com.hmdm.persistence.domain.Location;
import com.hmdm.persistence.domain.User;
import com.hmdm.rest.json.LookupItem;
import com.hmdm.rest.json.Response;
import com.hmdm.security.SecurityContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"Device Location"}, authorizations = {@Authorization("Bearer Token")})
@Singleton
@Path("/private/locations")
public class LocationResource {
    private UserDAO userDAO;
    private LocationDAO locationDAO;

    /**
     * <p>A logger to be used for logging the events.</p>
     */
    private static final Logger log = LoggerFactory.getLogger(LocationResource.class);

    /**
     * <p>A constructor required by Swagger.</p>
     */
    public LocationResource() {
    }

    @Inject
    public LocationResource(LocationDAO LocationDAO,
                            UserDAO userDAO) {
        this.userDAO = userDAO;
        this.locationDAO = LocationDAO;
    }

    // =================================================================================================================
    @ApiOperation(
            value = "Get all device locations",
            notes = "Gets the list of all available device locations",
            response = Group.class,
            responseContainer = "List"
    )
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLocations() {
        return Response.OK(this.locationDAO.getAllLocations());
    }

    // =================================================================================================================
    @ApiOperation(
            value = "Search locations",
            notes = "Search locations meeting the specified filter value",
            response = Group.class,
            responseContainer = "List"
    )
    @GET
    @Path("/search/{value}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchGroups(@PathParam("value") @ApiParam("A filter value") String value) {
        return Response.OK(this.locationDAO.getLocationByCode(value));
    }


    // =================================================================================================================
    @ApiOperation(
            value = "Create or update device location",
            notes = "Create a new device location (if id is not provided) or update existing one otherwise."
    )
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateLocation(Location location) {
        if (!SecurityContext.get().hasPermission("settings")) {
            log.error("Unauthorized attempt to update groups by user " +
                    SecurityContext.get().getCurrentUserName());
            return Response.PERMISSION_DENIED();
        }
        Location dbLocation = this.locationDAO.getLocationById(location.getId());
        if (dbLocation != null && !dbLocation.getId().equals(dbLocation.getId())) {
            return Response.DUPLICATE_ENTITY("error.duplicate.location");
        } else {
            if (location.getId() == null) {
                this.locationDAO.insertLocation(location);
            } else {
                this.locationDAO.updateLocation(location);
            }
            return Response.OK();
        }
    }

    // =================================================================================================================
    @ApiOperation(
            value = "Delete device location",
            notes = "Delete an existing device location"
    )
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeLocation(@PathParam("id") @ApiParam("Location ID") Integer id) {
        if (!SecurityContext.get().hasPermission("settings")) {
            log.error("Unauthorized attempt to update groups by user " +
                    SecurityContext.get().getCurrentUserName());
            return Response.PERMISSION_DENIED();
        }
        this.locationDAO.removeLocationById(id);
        return Response.OK();
    }
}
