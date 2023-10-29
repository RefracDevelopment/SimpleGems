package me.refracdevelopment.simplegems.utilities;

import com.cryptomorin.xseries.XMaterial;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utilities {

    public XMaterial getMaterial(String source) {
        XMaterial material;
        try {
            material = XMaterial.matchXMaterial(source).get();
        } catch (Exception e) {
            material = XMaterial.REDSTONE_BLOCK;
        }
        return material;
    }
}