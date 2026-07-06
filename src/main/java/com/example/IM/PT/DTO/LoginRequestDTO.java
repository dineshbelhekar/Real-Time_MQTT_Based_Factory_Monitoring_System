package com.example.IM.PT.DTO;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
public class LoginRequestDTO {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String employeeId;

}
