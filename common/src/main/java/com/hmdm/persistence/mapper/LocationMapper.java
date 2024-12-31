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

import com.hmdm.persistence.domain.Location;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface LocationMapper {

    List<Location> findAll();

    List<Location> findByCode(@Param("code") String code);

    Location findById(@Param("id") Integer id);

    @Insert({"INSERT INTO locations (code, name, state, latitude, longitude) " +
            "VALUES (#{code}, #{name}, #{state}, #{latitude}, #{longitude})"})
    @SelectKey( statement = "SELECT currval('locations_id_seq')", keyColumn = "id", keyProperty = "id",
                before = false, resultType = int.class )
    void insert(Location location);

    @Update({"UPDATE locations " +
            "SET code = #{code},name = #{name},state = #{state},latitude = #{latitude}, longitude=#{longitude}" +
            "WHERE id=#{id}"})
    void update(Location location);

    @Delete({"DELETE FROM locations WHERE id=#{id}"})
    void delete(@Param("id") Integer id);
}
