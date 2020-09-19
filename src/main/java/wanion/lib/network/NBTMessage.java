package wanion.lib.network;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import wanion.lib.WanionLib;
import wanion.lib.common.INBTMessage;

public class NBTMessage implements IMessage
{
	private int windowId;
	private NBTTagCompound nbtMessage;

	public NBTMessage() {}

	public NBTMessage(final int windowId, final NBTTagCompound nbtMessage)
	{
		this.windowId = windowId;
		this.nbtMessage = nbtMessage;
	}

	public int getWindowId()
	{
		return windowId;
	}

	public NBTTagCompound getNbtMessage()
	{
		return nbtMessage;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.windowId = ByteBufUtils.readVarInt(buf, 5);
		this.nbtMessage = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		ByteBufUtils.writeVarInt(buf, windowId, 5);
		ByteBufUtils.writeTag(buf, nbtMessage);
	}

	public static class Handler implements IMessageHandler<NBTMessage, NBTMessage>
	{
		@Override
		public NBTMessage onMessage(final NBTMessage nbtMessage, final MessageContext ctx)
		{
			WanionLib.proxy.getThreadListener().addScheduledTask(() -> WanionLib.proxy.receiveNBTMessage(nbtMessage, ctx));
			return WanionLib.proxy.isServer() ? new NBTMessage(nbtMessage.windowId, nbtMessage.nbtMessage) : null;
		}
	}
}