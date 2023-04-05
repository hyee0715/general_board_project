package com.hy.general_board_project.validator.validation.setting;

import javax.validation.GroupSequence;

@GroupSequence({NewPasswordConfirmValidationGroups.NotNullGroup.class, NewPasswordConfirmValidationGroups.IdentificationCheckWithNewPasswordGroup.class})
public interface NewPasswordConfirmValidationSequence {
}
