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

import com.hmdm.persistence.BroadcastDAO;
import com.hmdm.persistence.UnsecureDAO;
import com.hmdm.persistence.UserDAO;
import com.hmdm.persistence.domain.Customer;
import com.hmdm.persistence.domain.Group;
import com.hmdm.persistence.domain.Broadcast;
import com.hmdm.rest.json.Response;
import com.hmdm.security.SecurityContext;
import com.hmdm.service.EmailService;
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

@Api(tags = {"Broadcast meeting"}, authorizations = {@Authorization("Bearer Token")})
@Singleton
@Path("/private/broadcasts")
public class BroadcastResource {
    private UserDAO userDAO;
    private BroadcastDAO broadcastDAO;
    private UnsecureDAO unsecureDAO;
    private EmailService emailService;

    /**
     * <p>A logger to be used for logging the events.</p>
     */
    private static final Logger log = LoggerFactory.getLogger(BroadcastResource.class);

    /**
     * <p>A constructor required by Swagger.</p>
     */
    public BroadcastResource() {
    }

    @Inject
    public BroadcastResource(BroadcastDAO broadcastDAO,
                             UserDAO userDAO,UnsecureDAO unsecureDAO, EmailService emailService) {
        this.userDAO = userDAO;
        this.broadcastDAO = broadcastDAO;
        this.unsecureDAO = unsecureDAO;
        this.emailService = emailService;
    }

    // =================================================================================================================
    @ApiOperation(
            value = "Get all device Broadcast",
            notes = "Gets the list of all available device Broadcast",
            response = Group.class,
            responseContainer = "List"
    )
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBroadcasts() {
        return Response.OK(this.broadcastDAO.getAllBroadcasts());
    }

    // =================================================================================================================
    @ApiOperation(
            value = "Search Broadcast",
            notes = "Search Broadcast meeting the specified filter value",
            response = Group.class,
            responseContainer = "List"
    )
    @GET
    @Path("/search/{value}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchBroadcasts(@PathParam("value") @ApiParam("A filter value") String value) {
        return Response.OK(this.broadcastDAO.getBroadcastByCode(value));
    }


    // =================================================================================================================
    @ApiOperation(
            value = "Create or update device Broadcast",
            notes = "Create a new device Broadcast (if id is not provided) or update existing one otherwise."
    )
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBroadcast(Broadcast broadcast) {
        if (!SecurityContext.get().hasPermission("settings")) {
            log.error("Unauthorized attempt to update groups by user " +
                    SecurityContext.get().getCurrentUserName());
            return Response.PERMISSION_DENIED();
        }
        Broadcast dbBroadcast = this.broadcastDAO.getBroadcastByNumber(broadcast.getNumber());
        if (dbBroadcast != null && !dbBroadcast.getId().equals(broadcast.getId())) {
            return Response.DUPLICATE_ENTITY("error.duplicate.broadcast");
        } else {
            try {
                if (broadcast.getId() == null) {
                    this.broadcastDAO.insertBroadcast(broadcast);
                } else {
                    this.broadcastDAO.updateBroadcast(broadcast);
                }
                String to = broadcast.getLecturer() + "," + broadcast.getAttendees();
                emailService.sendEmail(to, emailService.getBroadcastEmailSubj(null, broadcast.getSubject()),
                        emailService.getBroadcastEmailBody(null, broadcast.getDescription()));
                return Response.OK();
            } catch (Exception e) {
                return Response.ERROR(e.getMessage());
            }
        }
    }

    // =================================================================================================================
    @ApiOperation(
            value = "Delete device Broadcast",
            notes = "Delete an existing device Broadcast"
    )
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeBroadcast(@PathParam("id") @ApiParam("Broadcast ID") Integer id) {
        if (!SecurityContext.get().hasPermission("settings")) {
            log.error("Unauthorized attempt to update groups by user " +
                    SecurityContext.get().getCurrentUserName());
            return Response.PERMISSION_DENIED();
        }
        this.broadcastDAO.removeBroadcastById(id);
        return Response.OK();
    }
}
