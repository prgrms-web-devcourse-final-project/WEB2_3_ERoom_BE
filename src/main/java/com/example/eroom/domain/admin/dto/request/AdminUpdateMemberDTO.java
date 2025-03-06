package com.example.eroom.domain.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateMemberDTO {
    private String name;

    public void setName(String name){
        this.name = name;
    }
}
