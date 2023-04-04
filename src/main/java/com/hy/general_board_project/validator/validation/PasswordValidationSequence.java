package com.hy.general_board_project.validator.validation;

import javax.validation.GroupSequence;

@GroupSequence({PasswordValidationGroups.NotNullGroup.class, PasswordValidationGroups.SizeCheckGroup.class,
        PasswordValidationGroups.PatternCheckGroup.class})
public interface PasswordValidationSequence {
}