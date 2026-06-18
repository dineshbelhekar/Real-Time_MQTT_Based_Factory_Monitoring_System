package com.example.IM.PT.request;

import com.example.IM.PT.Entity.User;
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
