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

package com.hmdm.persistence.mapper;

import com.hmdm.persistence.domain.Broadcast;
import com.hmdm.persistence.domain.Device;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface BroadcastMapper {

    List<Broadcast> findAll();

    List<Broadcast> findByCode(@Param("number") String number);

    Broadcast findById(@Param("id") Integer id);

    @Insert({"INSERT INTO broadcasts (type, number, subject, description, lecturer, attendees, starttime) " +
            "VALUES (#{type}, #{number}, #{subject}, #{description}, #{lecturer}, #{attendees}, #{startTime})"})
    @SelectKey( statement = "SELECT currval('broadcasts_id_seq')", keyColumn = "id", keyProperty = "id",
                before = false, resultType = int.class )
    void insert(Broadcast broadcast);

    int insertBroadcast(Broadcast broadcast);

    @Update({"UPDATE broadcasts " +
            "SET type = #{type},number = #{number},subject = #{subject},description = #{description}, lecturer=#{lecturer}, attendees=#{attendees}, starttime=#{startTime}" +
            "WHERE id=#{id}"})
    void update(Broadcast broadcast);

    @Delete({"DELETE FROM broadcasts WHERE id=#{id}"})
    void delete(@Param("id") Integer id);

    @Delete({"DELETE FROM broadcastdevices " +
            "WHERE broadcastId=#{broadcastId} "
            })
    void removeBroadcastDevicesByBroadcastId(@Param("broadcastId") int broadcastId);

    void insertBroadcastDevices(@Param("broadcastId") Integer broadcastId,
                               @Param("devices") List<Integer> devices);
}
