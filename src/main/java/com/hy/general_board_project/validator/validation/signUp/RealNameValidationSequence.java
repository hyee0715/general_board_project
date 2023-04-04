package com.hy.general_board_project.validator.validation.signUp;

import javax.validation.GroupSequence;

@GroupSequence({RealNameValidationGroups.NotNullGroup.class, RealNameValidationGroups.SizeCheckGroup.class,
        RealNameValidationGroups.PatternCheckGroup.class})
public interface RealNameValidationSequence {
}