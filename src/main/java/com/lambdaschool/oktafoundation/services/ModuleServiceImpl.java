package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.oktafoundation.models.Module;
import com.lambdaschool.oktafoundation.models.Course;
import com.lambdaschool.oktafoundation.repository.CourseRepository;
import com.lambdaschool.oktafoundation.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "moduleService")
public class ModuleServiceImpl implements ModuleService
{
    @Autowired
    private ModuleRepository modulerepos;

    @Autowired
    private CourseRepository courserepos;

    @Override
    public List<Module> findAll()
    {
        List<Module> modules = new ArrayList<>();
        modulerepos.findAll().iterator().forEachRemaining(modules::add);
        return modules;
    }

    @Override
    public Module findModulesById(long id)
    {
        return modulerepos.findById(id).orElseThrow(() -> new ResourceNotFoundException("Module with id " + id + " not found."));
    }

    @Override
    public Module findModulesByName(String name)
    {
        Module mm = modulerepos.findByModulenameIgnoreCase(name);
        if (mm == null)
        {
            throw new ResourceNotFoundException("Module name " + name + " not found.");
        }
        return mm;
    }


    @Override
    public Module save(long id, Module module)
    {
        Module newModule = new Module();
        if(newModule.getModuleid() != 0)
        {
            modulerepos.findById(newModule.getModuleid()).orElseThrow(() -> new ResourceNotFoundException("Module with id" + module.getModuleid() + " not found."));
            newModule.setModuleid(module.getModuleid());
        }
        newModule.setModulename(module.getModulename());
        newModule.setModuledescription(module.getModuledescription());
        newModule.setModulecontent(module.getModulecontent());

        Course course = courserepos.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course with id " + id + " not found."));
        if (course != null)
        {
            newModule.setCourse(course);
        }
        return modulerepos.save(newModule);
    }

    @Override
    public Module update(long id, Module module)
    {
        Module newModule = findModulesById(id);

        if(module.getModulename() != null)
        {
            newModule.setModulename(module.getModulename());
        }

        if(module.getModuledescription() != null)
        {
            newModule.setModuledescription(module.getModuledescription());
        }

        if(module.getModulecontent() != null)
        {
            newModule.setModulecontent(module.getModulecontent());
        }
        return modulerepos.save(newModule);
    }

    @Override
    public void delete(long id)
    {
        modulerepos.findById(id).orElseThrow(() -> new ResourceNotFoundException("Module with id " + id + " not found."));
        modulerepos.deleteById(id);
    }
}
