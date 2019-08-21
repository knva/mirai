package net.mamoe.mirai.utils.config;

import net.mamoe.mirai.MiraiServer;
import net.mamoe.mirai.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * YAML-TYPE CONFIG
 * Thread SAFE
 *
 * @author NaturalHG
 */
public class MiraiConfig extends MiraiConfigSection<Object> {

    private final File root;

    public MiraiConfig(@NotNull String configName) {
        this(new File(MiraiServer.getInstance().getParentFolder(), Objects.requireNonNull(configName)));
    }

    public MiraiConfig(@NotNull File file) {
        super();
        Objects.requireNonNull(file);
        /*if (!file.toURI().getPath().contains(MiraiServer.getInstance().getParentFolder().getPath())) {
            file = new File(MiraiServer.getInstance().getParentFolder().getPath(), file.getName());
        }*/

        this.root = file;

        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.parse();
    }

    public synchronized void save() {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(dumperOptions);
        String content = yaml.dump(this);
        try {
            Utils.writeFile(this.root, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void parse() {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(dumperOptions);
        this.clear();
        try {
            Map<String, Object> content = yaml.loadAs(Utils.readFile(this.root), LinkedHashMap.class);
            if (content != null) {
                this.putAll(content);
                System.out.println(this.keySet().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
