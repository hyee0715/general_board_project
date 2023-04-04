package com.hy.general_board_project.validator.validation.signUp;

import javax.validation.GroupSequence;

@GroupSequence({EmailValidationGroups.NotNullGroup.class, EmailValidationGroups.EmailCheckGroup.class})
public interface EmailValidationSequence {
}