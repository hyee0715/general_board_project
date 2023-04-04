package com.hy.general_board_project.validator.validation.setting;

import javax.validation.GroupSequence;

@GroupSequence({CurrentPasswordValidationGroups.NotNullGroup.class, CurrentPasswordValidationGroups.SizeCheckGroup.class,
        CurrentPasswordValidationGroups.PatternCheckGroup.class, CurrentPasswordValidationGroups.IdentificationCheckGroup.class})
public interface CurrentPasswordValidationSequence {
}