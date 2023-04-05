package com.hy.general_board_project.validator.validation.setting;

import javax.validation.GroupSequence;

@GroupSequence({WithdrawEmailValidationGroups.NotNullGroup.class, WithdrawEmailValidationGroups.EmailCheckGroup.class, WithdrawEmailValidationGroups.IdentificationCheckWithEmailGroup.class})
public interface WithdrawEmailValidationSequence {
}
