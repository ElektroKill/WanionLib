package wanion.lib.network;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import wanion.lib.WanionLib;
import wanion.lib.common.control.ControlsContainer;

public class ControlsContainerSync implements IMessage
{
	//private int windowId;
	private NBTTagCompound nbtTagCompound;

	public ControlsContainerSync() {}

	public ControlsContainerSync(final NBTTagCompound nbtTagCompound)
	{
		//this.windowId = windowId;
		this.nbtTagCompound = nbtTagCompound;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		//this.windowId = ByteBufUtils.readVarInt(buf, 5);
		this.nbtTagCompound = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		//ByteBufUtils.writeVarInt(buf, windowId, 5);
		ByteBufUtils.writeTag(buf, nbtTagCompound);
	}

	public static class Handler implements IMessageHandler<ControlsContainerSync, IMessage>
	{
		@Override
		public IMessage onMessage(final ControlsContainerSync controlsContainerSync, final MessageContext ctx)
		{
			final EntityPlayer entityPlayer = WanionLib.proxy.getEntityPlayerFromContext(ctx);
			if (entityPlayer != null && entityPlayer.openContainer instanceof ControlsContainer)
				((ControlsContainer) entityPlayer.openContainer).sync(controlsContainerSync.nbtTagCompound);
			return null;
		}
	}
}