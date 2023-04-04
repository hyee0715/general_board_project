package com.hy.general_board_project.validator.validation.setting;

import javax.validation.GroupSequence;

@GroupSequence({NewPasswordValidationGroups.NotNullGroup.class, NewPasswordValidationGroups.SizeCheckGroup.class, NewPasswordValidationGroups.IdentificationCheckWithCurrentPasswordGroup.class, NewPasswordValidationGroups.PatternCheckGroup.class})
public interface NewPasswordValidationSequence {
}
