package it.fktcod.ktykshrk.utils.visual;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
@SideOnly(Side.CLIENT)
public class ETessellator
{
    private final BufferBuilder buffer;
    private final WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();
    /** The static instance of the Tessellator. */
    private static final ETessellator INSTANCE = new ETessellator(2097152);

    public static ETessellator getInstance()
    {
        return INSTANCE;
    }

    public ETessellator(int bufferSize)
    {
        this.buffer = new BufferBuilder(bufferSize);
    }

    /**
     * Draws the data set up in this tessellator and resets the state to prepare for new drawing.
     */
    public void draw()
    {
        this.buffer.finishDrawing();
        this.vboUploader.draw(this.buffer);
    }

    public BufferBuilder getBuffer()
    {
        return this.buffer;
    }
}
