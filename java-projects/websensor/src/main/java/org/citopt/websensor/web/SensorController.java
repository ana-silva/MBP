package org.citopt.websensor.web;

import java.util.Map;
import javax.servlet.ServletContext;
import org.bson.types.ObjectId;
import org.citopt.websensor.domain.Sensor;
import org.citopt.websensor.repository.DeviceRepository;
import org.citopt.websensor.repository.ScriptRepository;
import org.citopt.websensor.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/sensor")
public class SensorController {

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ScriptRepository scriptRepository;

    @Autowired
    private ServletContext servletContext;

    @RequestMapping(method = RequestMethod.GET)
    public String viewSensor(Map<String, Object> model) {
        Sensor sensorForm = new Sensor();
        model.put("sensorForm", sensorForm);

        model.put("sensors", sensorRepository.findAll());
        model.put("devices", deviceRepository.findAll());
        model.put("scripts", scriptRepository.findAll());
        model.put("uriSensor", servletContext.getContextPath() + "/sensor");
        model.put("uriDevice", servletContext.getContextPath() + "/device");
        model.put("uriScript", servletContext.getContextPath() + "/script");

        return "sensor";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processRegistration(
            @ModelAttribute("sensorForm") Sensor sensor,
            Map<String, Object> model) {
        System.out.println(sensor);

        sensor = sensorRepository.insert(sensor);

        return "redirect:" + "/sensor" + "/" + sensor.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String viewSensorById(
            @PathVariable("id") ObjectId id,
            Map<String, Object> model) {
        Sensor sensor = sensorRepository.findOne(id.toString());
        model.put("sensor", sensor);
        model.put("sensorForm", sensor);
        model.put("devices", deviceRepository.findAll());
        model.put("scripts", scriptRepository.findAll());

        String uriSensor = servletContext.getContextPath() + "/sensor"
                + "/" + id;
        model.put("uriEdit", uriSensor + "/edit");
        model.put("uriDelete", uriSensor + "/delete");
        model.put("uriCancel", uriSensor);
        model.put("uriSensor", uriSensor);
        model.put("uriDevice", servletContext.getContextPath() + "/device");
        model.put("uriScript", servletContext.getContextPath() + "/script");

        return "sensor/id";
    }

    @RequestMapping(value = "/{id}" + "/edit", method = RequestMethod.POST)
    public String processEditSensor(
            @ModelAttribute("sensorForm") Sensor sensor,
            Map<String, Object> model) {
        sensorRepository.save(sensor);

        return "redirect:" + "/sensor" + "/" + sensor.getId();
    }

    @RequestMapping(value = "/{id}" + "/delete", method = RequestMethod.GET)
    public String processDeleteSensor(
            @PathVariable("id") ObjectId id,
            Map<String, Object> model) {
        sensorRepository.delete(id.toString());

        return "redirect:" + "/sensor";
    }

}