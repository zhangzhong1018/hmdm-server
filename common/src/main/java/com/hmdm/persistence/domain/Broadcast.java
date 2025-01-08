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

package com.hmdm.persistence.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hmdm.rest.json.LookupItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel(description = "A broadcast")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Broadcast implements Serializable {

    private static final long serialVersionUID = 3868700195035955790L;

    @ApiModelProperty("A broadcast ID")
    private Integer id;

    @ApiModelProperty("A broadcast type")
    private Integer type;
    @ApiModelProperty("A broadcast number")
    private String number;
    @ApiModelProperty("A broadcast subject")
    private String subject;
    @ApiModelProperty("A broadcast description")
    private String description;
    @ApiModelProperty("A broadcast lecturer")
    private String lecturer;
    @ApiModelProperty("A broadcast attendees")
    private String attendees;
    @ApiModelProperty("A broadcast startTime")
    private Long startTime;

    @ApiModelProperty("A list of devices assigned to broadcast")
    private List<LookupItem> devices;

    // Helper fields, not persisted
    @ApiModelProperty(hidden = true)
    private String time;


    public Broadcast() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getAttendees() {
        return attendees;
    }

    public void setAttendees(String attendees) {
        this.attendees = attendees;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public List<LookupItem> getDevices() {
        return devices;
    }

    public void setDevices(List<LookupItem> devices) {
        this.devices = devices;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
