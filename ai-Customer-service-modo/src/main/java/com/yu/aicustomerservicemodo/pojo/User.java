package com.yu.aicustomerservicemodo.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BasePojo {

    private String account_;

    private String userName;

    private String phone_;
}
