package com.lambdaschool.oktafoundation;

import com.lambdaschool.oktafoundation.models.*;
import com.lambdaschool.oktafoundation.services.RoleService;
import com.lambdaschool.oktafoundation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * SeedData puts both known and random data into the database. It implements CommandLineRunner.
 * <p>
 * CoomandLineRunner: Spring Boot automatically runs the run method once and only once
 * after the application context has been loaded.
 */
@Transactional
@ConditionalOnProperty(
    prefix = "command.line.runner",
    value = "enabled",
    havingValue = "true",
    matchIfMissing = true)
@Component
public class SeedData
    implements CommandLineRunner
{
    /**
     * Connects the Role Service to this process
     */
    @Autowired
    RoleService roleService;

    /**
     * Connects the user service to this process
     */
    @Autowired
    UserService userService;

    /**
     * Generates test, seed data for our application
     * First a set of known data is seeded into our database.
     * Second a random set of data using Java Faker is seeded into our database.
     * Note this process does not remove data from the database. So if data exists in the database
     * prior to running this process, that data remains in the database.
     *
     * @param args The parameter is required by the parent interface but is not used in this process.
     */
    @Transactional
    @Override
    public void run(String[] args) throws
                                   Exception
    {
        roleService.deleteAll();
        Role r1 = new Role("admin");
        Role r2 = new Role("teacher");
        Role r3 = new Role("student");

        r1 = roleService.save(r1);
        r2 = roleService.save(r2);
        r3 = roleService.save(r3);

        User u1 = new User("llama001@maildrop.cc", "llama001@email.com", "llama", "001", "(987)654-3210");
        u1.getRoles()
            .add(new UserRoles(u1,
                r1));
        u1.getPrograms()
                .add(new Program("Coding", "12th grade", "Something Something Doing this stuff"));
        userService.save(u1);

        User u2 = new User("barnbarn@maildrop.cc", "barnbarn@maildrop.cc","barnbarn", "teacher", "(987)665-4423");
        u2.getRoles()
            .add(new UserRoles(u2,
                r2));
        userService.save(u2);

        User u3 = new User("johndoe@email.co", "barnbarn@maildrop.cc", "John", "Doe", "(123)456-7890");
        u3.getRoles()
                .add(new UserRoles(u3, r3));

        User u4 = new User("fakeadmin2@email.com", "barnbarn@maildrop.cc", "Fake", "Admin", "(456)123-7890");
        u4.getRoles()
                .add(new UserRoles(u4,
                        r1));
        // The following is an example user!
        /*
        // admin, data, user
        User u1 = new User("admin",
            "password",
            "admin@lambdaschool.local");
        u1.getRoles()
            .add(new UserRoles(u1,
                r1));
        u1.getRoles()
            .add(new UserRoles(u1,
                r2));
        u1.getRoles()
            .add(new UserRoles(u1,
                r3));
        u1.getUseremails()
            .add(new Useremail(u1,
                "admin@email.local"));
        u1.getUseremails()
            .add(new Useremail(u1,
                "admin@mymail.local"));

        userService.save(u1);
        */
    }
}