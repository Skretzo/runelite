/*
 * Copyright (c) 2019, Weird Gloop <admin@weirdgloop.org>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.runelite.client.plugins.crowdsourcing.crafting;

import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.Skill;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.crowdsourcing.CrowdsourcingManager;

public class CrowdsourcingCrafting
{
	private static final String FIRING_SUCCESS_PREFIX = "You remove the ";
	private static final String FIRING_SUCCESS_SUFFIX = " from the oven.";
	private static final String FIRING_FAILURE_PREFIX = "The ";
	private static final String FIRING_FAILURE_SUFFIX = " cracks in the oven.";

	private int lastGameObjectClicked;

	@Inject
	private CrowdsourcingManager manager;

	@Inject
	private Client client;

	@Subscribe
	public void onChatMessage(final ChatMessage event)
	{
		if (!ChatMessageType.SPAM.equals(event.getType()))
		{
			return;
		}

		final String message = event.getMessage();

		if ((message.startsWith(FIRING_SUCCESS_PREFIX) && message.endsWith(FIRING_SUCCESS_SUFFIX)) ||
			(message.startsWith(FIRING_FAILURE_PREFIX) && message.endsWith(FIRING_FAILURE_SUFFIX)))
		{
			final int craftingLevel = client.getBoostedSkillLevel(Skill.CRAFTING);
			CraftingData data = new CraftingData(message, lastGameObjectClicked, craftingLevel);
			manager.storeEvent(data);
		}
	}

	@Subscribe
	public void onMenuOptionClicked(final MenuOptionClicked menuOptionClicked)
	{
		final MenuAction action = menuOptionClicked.getMenuAction();
		if (action.equals(MenuAction.ITEM_USE_ON_GAME_OBJECT) ||
			action.equals(MenuAction.GAME_OBJECT_FIRST_OPTION) ||
			action.equals(MenuAction.GAME_OBJECT_SECOND_OPTION) ||
			action.equals(MenuAction.GAME_OBJECT_THIRD_OPTION) ||
			action.equals(MenuAction.GAME_OBJECT_FOURTH_OPTION) ||
			action.equals(MenuAction.GAME_OBJECT_FIFTH_OPTION))
		{
			lastGameObjectClicked = menuOptionClicked.getId();
		}
	}
}
