package it.fktcod.ktykshrk.utils.frame;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.ForgeMod;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.system.WebUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.value.Value;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class Config extends JFrame {

    public static String Cloudurl;
    public static String ReturnData;

    private static JsonParser jsonParser = new JsonParser();

    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void Client(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Config frame = new Config();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Config() {
        setTitle("Cloud-Config-Loader");
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JTextArea textArea = new JTextArea();
        textArea.setBackground(Color.WHITE);
        textArea.setToolTipText("Fill With Config-Author");
        textArea.setBounds(0, 10, 434, 24);
        contentPane.add(textArea);

        JTextArea textArea2 = new JTextArea();
        textArea2.setBackground(Color.WHITE);
        textArea2.setToolTipText("Fill With Config-Name");
        textArea2.setBounds(0, 40, 434, 24);
        contentPane.add(textArea2);

        JButton btnNewButton = new JButton("Load");
        btnNewButton.setBounds(10, 74, 103, 25);
        contentPane.add(btnNewButton);

    }
 



    public static void loadHacks(String parse) {
        JsonObject jsonObject = null;
        try {
            jsonObject = (JsonObject) jsonParser.parse(parse);


            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                Module module = HackManager.getHack(entry.getKey());

                if (module == null)
                    continue;

                JsonObject jsonObjectHack = (JsonObject) entry.getValue();
                module.setKey(jsonObjectHack.get("key").getAsInt());
                module.setToggled(jsonObjectHack.get("toggled").getAsBoolean());

                if (module.getValues().isEmpty())
                    continue;

                for (Value value : module.getValues()) {
                    if (value instanceof BooleanValue)
                        value.setValue(jsonObjectHack.get(value.getName()).getAsBoolean());
                    if (value instanceof NumberValue)
                        value.setValue(jsonObjectHack.get(value.getName()).getAsDouble());
                    if (value instanceof ModeValue) {
                        ModeValue modeValue = (ModeValue) value;
                        for (Mode mode : modeValue.getModes())
                            mode.setToggled(jsonObjectHack.get(mode.getName()).getAsBoolean());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

