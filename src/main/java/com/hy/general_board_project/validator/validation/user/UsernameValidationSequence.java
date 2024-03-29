package com.hy.general_board_project.validator.validation.user;

import javax.validation.GroupSequence;

@GroupSequence({UsernameValidationGroups.NotNullGroup.class, UsernameValidationGroups.SizeCheckGroup.class,
        UsernameValidationGroups.PatternCheckGroup.class})
public interface UsernameValidationSequence {
}