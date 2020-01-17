package com.suppergerrie2.consolesideplacement;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(
        modid = ConsoleSidePlacement.MOD_ID,
        name = ConsoleSidePlacement.MOD_NAME,
        version = ConsoleSidePlacement.VERSION
)
@Mod.EventBusSubscriber(modid = ConsoleSidePlacement.MOD_ID)
public class ConsoleSidePlacement {

    public static final String MOD_ID = "sconsolesideplacement";
    public static final String MOD_NAME = "ConsoleSidePlacement";
    public static final String VERSION = "1.0";

    @SubscribeEvent
    public static void itemRightClickEvent(PlayerInteractEvent.RightClickItem event) {
        World world = event.getWorld();

        if (event.getEntityPlayer() == null) {
            return;
        }

        EntityPlayer player = event.getEntityPlayer();

        ItemStack itemStack = event.getItemStack();

        Vec3d look = player.getLookVec();

        look = look.add(look.normalize().scale(1.5));

        BlockPos pos = new BlockPos(look.x + player.posX, look.y + player.posY + player.getEyeHeight(), look.z + player.posZ);

        if(pos.getY()!=player.getPosition().down().getY()) return;

        EnumFacing side = EnumFacing.getDirectionFromEntityLiving(pos.up(), player).getOpposite();

        if(!world.getBlockState(pos.offset(side.getOpposite())).isSideSolid(world, pos.offset(side), side)) return;

        int metadata = 0;
        int count = 0;
        if (player.isCreative()) {
            metadata = itemStack.getMetadata();
            count = itemStack.getCount();
        }

        if(!world.isRemote) itemStack.onItemUse(player, event.getWorld(), pos, event.getHand(), side, 0, 0, 0);
        player.swingArm(event.getHand());

        if (player.isCreative()) {
            itemStack.setItemDamage(metadata);
            itemStack.setCount(count);
        }
    }
}
