package mcjty.lib.gui.widgets;

import mcjty.lib.gui.Window;
import mcjty.lib.gui.events.ChoiceEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageChoiceLabel extends ImageLabel<ImageChoiceLabel> {
    private List<String> choiceList = new ArrayList<String>();
    private List<List<String>> tooltipList = new ArrayList<List<String>>();
    private List<ResourceLocation> imageList = new ArrayList<ResourceLocation>();
    private List<Integer> uList = new ArrayList<Integer>();
    private List<Integer> vList = new ArrayList<Integer>();

    private int currentChoice = -1;
    private List<ChoiceEvent> choiceEvents = null;

    public ImageChoiceLabel(Minecraft mc, Gui gui) {
        super(mc, gui);
    }

    @Override
    public List<String> getTooltips() {
        if (currentChoice == -1) {
            return super.getTooltips();
        } else {
            return tooltipList.get(currentChoice);
        }
    }

    public ImageChoiceLabel addChoice(String choice, String tooltips, ResourceLocation image, int u, int v) {
        choiceList.add(choice);
        tooltipList.add(Arrays.asList(StringUtils.split(tooltips, "\n")));
        imageList.add(image);
        uList.add(u);
        vList.add(v);
        if (currentChoice == -1) {
            currentChoice = 0;
            setCurrentChoice(currentChoice);
        }
        return this;
    }

    public void clear() {
        choiceList.clear();
        tooltipList.clear();
        imageList.clear();
        uList.clear();
        vList.clear();
        currentChoice = -1;
    }

    @Override
    public Widget mouseClick(Window window, int x, int y, int button) {
        if (isEnabledAndVisible()) {
            currentChoice++;
            if (currentChoice >= choiceList.size()) {
                currentChoice = 0;
            }
            setCurrentChoice(currentChoice);
            fireChoiceEvents(choiceList.get(currentChoice));
        }
        return null;
    }


    public void setCurrentChoice(int currentChoice) {
        this.currentChoice = currentChoice;
        setImage(imageList.get(currentChoice), uList.get(currentChoice), vList.get(currentChoice));
    }

    public int findChoice(String choice) {
        if (choice == null) {
            return -1;
        }
        int i = 0;
        for (String s : choiceList) {
            if (choice.equals(s)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public void setCurrentChoice(String choice) {
        int idx = findChoice(choice);
        if (idx != -1) {
            setCurrentChoice(idx);
        }
    }

    public int getCurrentChoiceIndex() {
        return currentChoice;
    }

    public String getCurrentChoice() {
        if (currentChoice == -1) {
            return null;
        }
        return choiceList.get(currentChoice);
    }

    public ImageChoiceLabel addChoiceEvent(ChoiceEvent event) {
        if (choiceEvents == null) {
            choiceEvents = new ArrayList<ChoiceEvent>();
        }
        choiceEvents.add(event);
        return this;
    }

    public void removeChoiceEvent(ChoiceEvent event) {
        if (choiceEvents != null) {
            choiceEvents.remove(event);
        }
    }

    private void fireChoiceEvents(String choice) {
        if (choiceEvents != null) {
            for (ChoiceEvent event : choiceEvents) {
                event.choiceChanged(this, choice);
            }
        }
    }

}
