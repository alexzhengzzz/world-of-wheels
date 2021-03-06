package com.annotation;

import com.enums.Role;
import com.enums.RoleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermissionChecker {
    Role requiredRole() default Role.ANONYMOUS; // API for checking permissions
    RoleType requiredRoleType() default RoleType.GUEST; // API for exact role type
}
