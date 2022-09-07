package com.hcl.commerce.integration.bazaarVoice.command;

import com.ibm.commerce.command.ControllerCommand;

public interface BazaarVoiceGetContentCmd extends ControllerCommand {
	public static final String COPYRIGHT = "Copyright [2021] [HCL America, Inc.]";
	public static final String NAME = BazaarVoiceGetContentCmd.class.getName();
	public static final String defaultCommandClassName = BazaarVoiceGetContentCmdImpl.class.getName();
}
