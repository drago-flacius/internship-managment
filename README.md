# Internship Management System

This is a web app  made to help students, companies, and faculty manage the whole mandatory internship process at the faculty. 
You can register, apply for internships, post them, and track everything — all in one place. Built with Spring Boot, React, PostgreSQL, and a bit of Spring Security for safety.  

## Features

- **Roles:** Students, Companies, and Admin staff (who handle approvals).  
- **Student side:** Sign up, browse internships, apply, track your applications.  
- **Company side:** Post internships, see who applied, give feedback.  
- **Administrative worker side:** Can approve/reject student applications and company postings to keep things smooth.  

## Tech Stack

- **Backend:** Java + Spring Boot + Spring Security + Spring Data JPA  
- **Frontend:** React + Tailwind 
- **Database:** PostgreSQL  
- **Build:** Maven  

## How to run it

Clone the repo: `git clone <your-repo-url>`  
Set up a PostgreSQL database and update the `application.properties` file in the backend with your DB URL, username, and password.  
Run the backend: `mvn clean install` then `mvn spring-boot:run`  
Go to the `frontend` folder, run `npm install`, then `npm start` to start React.  
Open `http://localhost:3000`

---

Made this as part of my learning and fun with Java + React. Any feedback or tips are welcome!
