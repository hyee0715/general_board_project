package com.hy.general_board_project.validator.validation;

import javax.validation.GroupSequence;

@GroupSequence({NicknameValidationGroups.NotNullGroup.class, NicknameValidationGroups.SizeCheckGroup.class,
        NicknameValidationGroups.PatternCheckGroup.class})
public interface NicknameValidationSequence {
}