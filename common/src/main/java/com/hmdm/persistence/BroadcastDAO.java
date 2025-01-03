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

package com.hmdm.persistence;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hmdm.persistence.domain.Broadcast;
import com.hmdm.persistence.mapper.BroadcastMapper;
import com.hmdm.rest.json.LookupItem;
import com.hmdm.security.SecurityContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author isv
 */
@Singleton
public class BroadcastDAO {

    private final UnsecureDAO unsecureDAO;
    private final UserDAO userDAO;
    private final BroadcastMapper mapper;

    @Inject
    public BroadcastDAO(UnsecureDAO unsecureDAO, UserDAO userDAO, BroadcastMapper mapper) {
        this.unsecureDAO = unsecureDAO;
        this.userDAO = userDAO;
        this.mapper = mapper;
    }

    public boolean hasAccess() {
        return SecurityContext.get().getCurrentUser().map(u -> {
            if (unsecureDAO.isSingleCustomer()) {
                if (!u.getUserRole().isSuperAdmin() && !userDAO.isOrgAdmin(u)) {
                    return false;
                }
            } else if (!u.getUserRole().isSuperAdmin()) {
                return false;
            }
            return true;
        }).orElse(false);
    }

    void checkAccess() {
        if (!hasAccess()) {
            throw new IllegalArgumentException("Operation not allowed");
        }
    }

    public List<Broadcast> getAllBroadcasts() {
        checkAccess();
        return mapper.findAll();
    }

    public List<Broadcast> getBroadcastByCode(String code) {
        checkAccess();
        return mapper.findByCode(code);
    }

    public void insertBroadcast(Broadcast broadcast) {
        checkAccess();
        mapper.insertBroadcast(broadcast);
        if (broadcast.getDevices() != null && !broadcast.getDevices().isEmpty()) {
            this.mapper.insertBroadcastDevices(
                    broadcast.getId(), broadcast.getDevices().stream().map(LookupItem::getId).collect(Collectors.toList())
            );
        }
    }

    public void updateBroadcast(Broadcast broadcast) {
        checkAccess();
        mapper.update(broadcast);
        mapper.removeBroadcastDevicesByBroadcastId(broadcast.getId());
        if (broadcast.getDevices() != null && !broadcast.getDevices().isEmpty()) {
            this.mapper.insertBroadcastDevices(
                    broadcast.getId(), broadcast.getDevices().stream().map(LookupItem::getId).collect(Collectors.toList())
            );
        }
    }

    public void removeBroadcastById(Integer id) {
        checkAccess();
        mapper.delete(id);
    }

    public Broadcast getBroadcastById(Integer id) {
        checkAccess();
        return mapper.findById(id);
    }

}
