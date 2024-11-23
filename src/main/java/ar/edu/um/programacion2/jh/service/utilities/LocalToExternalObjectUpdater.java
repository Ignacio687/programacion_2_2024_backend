package ar.edu.um.programacion2.jh.service.utilities;

import ar.edu.um.programacion2.jh.domain.*;
import ar.edu.um.programacion2.jh.service.dto.DeviceDTO;
import java.util.List;
import org.hibernate.ObjectNotFoundException;

public interface LocalToExternalObjectUpdater {
    Device updateAndSaveDevice(DeviceDTO externalDevice, String supplier);
    Device updateAndSaveDevice(Device localDevice, DeviceDTO externalDevice);
    Characteristic updateAndSaveCharacteristic(Characteristic externalCharacteristic);
    Characteristic updateAndSaveCharacteristic(Characteristic localCharacteristic, Characteristic externalCharacteristic);
    Extra updateAndSaveExtra(Extra externalExtra);
    Extra updateAndSaveExtra(Extra localExtra, Extra externalExtra);
    Option updateAndSaveOption(Option externalOption);
    Option updateAndSaveOption(Option localOption, Option externalOption);
    Customization updateAndSaveCustomization(Customization externalCustomization);
    Customization updateAndSaveCustomization(Customization localCustomization, Customization externalCustomization);
}
