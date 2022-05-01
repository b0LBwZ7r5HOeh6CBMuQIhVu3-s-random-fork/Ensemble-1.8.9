package it.fktcod.ktykshrk.utils.visual;

import it.fktcod.ktykshrk.utils.visual.EnumChatFormatting;
import net.minecraft.client.Minecraft;

public class AvaritiaColorUtil
{
    public static final EnumChatFormatting[] fabulousness;
    public static final EnumChatFormatting[] sanic;
    public static final EnumChatFormatting[] Azrael;
    public static final EnumChatFormatting[] Sense;

    public AvaritiaColorUtil()
    {
    }

    public static String makeFabulous(String input)
    {
        return ludicrousFormatting(input, fabulousness, 130.0D, 1, 1);
    }

    public static String makeSANIC(String input)
    {
        return ludicrousFormatting(input, sanic, 100.0D, 2, 1);
    }
    
    public static String makeAzrael(String input)
    {
        return ludicrousFormatting(input, Azrael, 200.0D, 2, 1);
    }
    
    public static String ludicrousFormatting(String input, EnumChatFormatting[] colours, double delay, int step, int posstep)
    {
        StringBuilder sb = new StringBuilder(input.length() * 3);

        if (delay <= 0.0D)
        {
            delay = 0.001D;
        }

        int offset = (int)Math.floor((double)Minecraft.getSystemTime() / delay) % colours.length;

        for (int i = 0; i < input.length(); ++i)
        {
            char c = input.charAt(i);
            int col = (i * posstep + colours.length - offset) % colours.length;
            sb.append(colours[col].toString());
            sb.append(c);
        }

        return sb.toString();
    }

    static
    {
        fabulousness = new EnumChatFormatting[] {EnumChatFormatting.RED, EnumChatFormatting.GOLD, EnumChatFormatting.YELLOW, EnumChatFormatting.GREEN, EnumChatFormatting.AQUA, EnumChatFormatting.BLUE, EnumChatFormatting.LIGHT_PURPLE};
        Azrael = new EnumChatFormatting[] {EnumChatFormatting.BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.AQUA, EnumChatFormatting.AQUA, EnumChatFormatting.AQUA, EnumChatFormatting.AQUA, EnumChatFormatting.YELLOW, EnumChatFormatting.YELLOW, EnumChatFormatting.YELLOW,};
        sanic = new EnumChatFormatting[] {EnumChatFormatting.BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.WHITE, EnumChatFormatting.BLUE, EnumChatFormatting.WHITE, EnumChatFormatting.WHITE, EnumChatFormatting.BLUE, EnumChatFormatting.WHITE, EnumChatFormatting.WHITE, EnumChatFormatting.BLUE, EnumChatFormatting.RED, EnumChatFormatting.WHITE, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY};
        Sense = new EnumChatFormatting[] {EnumChatFormatting.DARK_GREEN, EnumChatFormatting.BOLD};
    }
}
