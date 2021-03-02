# Java Spring Reach LMS - Team/a Backend Documentation

## Introduction

This is database schema which included users, user emails, user roles, program, course, module, admin, student, teacher models. This Java Spring REST API application will provide endpoints for users to read various data sets contained in the application's data. This application will also form the basis for a user authentication with okta and resource authorization to allow only specific featuresets depending on the user role.

### Database layout

The table layout is similar to the common @ManyToMany annotation but with the following exceptions:

* Join tables such as userroles, studentcourses, teachercourses is explicitly created. This allows us to add additional columns to the join table
* Since we are creating the join table ourselves, the Many to Many relationship that formed the join table is now two Many to One relationships
* All tables now have audit fields (CREATED BY, CREATED DATE, LASTMODIFIED BY, LASTMODIFIED DATE)

Thus the new table layout is as follows

* User is the driving table.
* Programs have a Many-To-One relationship with User. Each User (ADMIN) has many user programs combinations. Each user program combination has only one User (ADMIN).
* Roles have a Many-To-Many relationship with Users.
---------

* Student is the driving table.
* Courses have Many-To-Many relationship with Student
-------

* Program is the driving table.
* Courses have Many-To-One realtionship with Program. Each Program have many admin courses combinations. Each program courses combination has only one program.

------
* Course is the driving table.
* Students have Many-To-Many relationship with Courses.
* Teachers have Many-To-Many realtionship with Courses

![Image of Database Layout](usersfinaldb.png)

Using the provided data, expand each endpoint below to see the output it generates.

---

<details>
<summary>http://localhost:2019/roles/roles</summary>

```JSON
[
    {
        "roleid": 1,
        "name": "ADMIN",
        "users": [
            {
                "user": {
                    "userid": 4,
                    "username": "llama001@maildrop.cc",
                    "email": "llama001@email.com",
                    "firstname": "llama",
                    "lastname": "001",
                    "phonenumber": "(987)654-3210",
                    "programs": []
                }
            },
            {
                "user": {
                    "userid": 7,
                    "username": "llama007@maildrop.cc",
                    "email": null,
                    "firstname": null,
                    "lastname": null,
                    "phonenumber": null,
                    "programs": []
                }
            }
        ]
    },
    {
        "roleid": 2,
        "name": "TEACHER",
        "users": [
            {
                "user": {
                    "userid": 6,
                    "username": "barnbarn@maildrop.cc",
                    "email": "barnbarn@maildrop.cc",
                    "firstname": "barnbarn",
                    "lastname": "teacher",
                    "phonenumber": "(987)665-4423",
                    "programs": []
                }
            }
        ]
    },
    {
        "roleid": 3,
        "name": "STUDENT",
        "users": []
    }
]
```

</details>

<details>
<summary>http://localhost:2019/roles/role/1</summary>

```JSON
{
    "roleid": 1,
    "name": "ADMIN",
    "users": [
        {
            "user": {
                "userid": 4,
                "username": "llama001@maildrop.cc",
                "email": "llama001@email.com",
                "firstname": "llama",
                "lastname": "001",
                "phonenumber": "(987)654-3210",
                "programs": []
            }
        },
        {
            "user": {
                "userid": 7,
                "username": "llama007@maildrop.cc",
                "email": null,
                "firstname": null,
                "lastname": null,
                "phonenumber": null,
                "programs": []
            }
        }
    ]
}
```

</details>

<details>
<summary>http://localhost:2019/roles/role/name/teacher</summary>

```JSON
{
    "roleid": 2,
    "name": "TEACHER",
    "users": [
        {
            "user": {
                "userid": 6,
                "username": "barnbarn@maildrop.cc",
                "email": "barnbarn@maildrop.cc",
                "firstname": "barnbarn",
                "lastname": "teacher",
                "phonenumber": "(987)665-4423",
                "programs": []
            }
        }
    ]
}
```

</details>

<details>
<summary>POST http://localhost:2019/roles/role</summary>

DATA

```JSON
{
    "name" : "ANewRole"
}
```

OUTPUT

```TEXT
Status CREATED

Location Header: http://localhost:2019/roles/role/16
```

</details>

<details>
<summary>http://localhost:2019/roles/role/name/anewrole</summary>

```JSON
{
    "roleid": 16,
    "name": "ANEWROLE",
    "users": []
}
```

</details>

<details>
<summary>PUT http://localhost:2019/roles/role/16</summary>

DATA

```JSON
{
    "name" : "ANewRole"
}
```

OUTPUT

```TEXT
Status OK
```

</details>

---

<details>
<summary>http://localhost:2019/users/users</summary>

```JSON
[
    {
        "userid": 4,
        "username": "llama001@maildrop.cc",
        "email": "llama001@email.com",
        "firstname": "llama",
        "lastname": "001",
        "phonenumber": "(987)654-3210",
        "programs": [],
        "roles": [
            {
                "role": {
                    "roleid": 1,
                    "name": "ADMIN"
                }
            }
        ]
    },
    {
        "userid": 6,
        "username": "barnbarn@maildrop.cc",
        "email": "barnbarn@maildrop.cc",
        "firstname": "barnbarn",
        "lastname": "teacher",
        "phonenumber": "(987)665-4423",
        "programs": [],
        "roles": [
            {
                "role": {
                    "roleid": 2,
                    "name": "TEACHER"
                }
            }
        ]
    },
    {
        "userid": 7,
        "username": "llama007@maildrop.cc",
        "email": null,
        "firstname": null,
        "lastname": null,
        "phonenumber": null,
        "programs": [],
        "roles": [
            {
                "role": {
                    "roleid": 1,
                    "name": "ADMIN"
                }
            }
        ]
    }
]
```

</details>

<details>
<summary>http://localhost:2019/users/user/7</summary>

```JSON
{
    "userid": 7,
    "username": "llama007@maildrop.cc",
    "email": null,
    "firstname": null,
    "lastname": null,
    "phonenumber": null,
    "programs": [],
    "roles": [
        {
            "role": {
                "roleid": 1,
                "name": "ADMIN"
            }
        }
    ]
}
```

</details>

<details>
<summary>http://localhost:2019/users/user/name/llama001@maildrop.cc</summary>

```JSON
{
    "userid": 4,
    "username": "llama001@maildrop.cc",
    "email": "llama001@email.com",
    "firstname": "llama",
    "lastname": "001",
    "phonenumber": "(987)654-3210",
    "programs": [],
    "roles": [
        {
            "role": {
                "roleid": 1,
                "name": "ADMIN"
            }
        }
    ]
}
```

</details>

<details>
<summary>http://localhost:2019/users/user/name/like/barn</summary>

```JSON
[
    {
        "userid": 6,
        "username": "barnbarn@maildrop.cc",
        "email": "barnbarn@maildrop.cc",
        "firstname": "barnbarn",
        "lastname": "teacher",
        "phonenumber": "(987)665-4423",
        "programs": [],
        "roles": [
            {
                "role": {
                    "roleid": 2,
                    "name": "TEACHER"
                }
            }
        ]
    }
]
```

</details>

<details>
<summary>POST http://localhost:2019/users/user</summary>

DATA

```JSON
{
        "username": "SomeNewUser",
        "email": "somenewuser@maildrop.cc",
        "firstname": "someonenew",
        "lastname": "anonymous",
        "phonenumber": "(987)665-4423",
        "programs": [],
        "roles": [
            {
                "role": {
                    "roleid": 2,
                    "name": "TEACHER"
                }
            }
        ]
}
```

OUTPUT

```TEXT
No Body Data

Location Header: http://localhost:2019/users/user/9
Status 201 Created
```

</details>

<details>
<summary>http://localhost:2019/users/user/name/SomeNewUser</summary>

</details>

<details>
<summary>PUT http://localhost:2019/users/user/9</summary>

DATA

```JSON
{
        "username": "UpdatedNewUser",
        "email": "somenewuser@maildrop.cc",
        "firstname": "someonenew",
        "lastname": "anonymous",
        "phonenumber": "(987)665-4423",
        "programs": [],
        "roles": [
            {
                "role": {
                    "roleid": 3
                }
            }
        ]
}
```

OUTPUT

```TEXT
No Body Data

Status OK
```

</details>


<details>
<summary>PATCH http://localhost:2019/users/user/7</summary>

DATA

```JSON
{
    "username": "BarnBarn",
    "email": "cinabun@lambdaschool.home"
}
```

OUTPUT

```TEXT
No Body Data

Status OK
```

</details>


</details>

<details>

<summary>DELETE http://localhost:2019/users/user/7</summary>

```TEXT
No Body Data

Status OK
```

</details>
