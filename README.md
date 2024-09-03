Proiect: Aplicație Online pentru Învățarea unei Limbi Străine

Această aplicație web permite utilizatorilor să învețe limbi străine printr-o serie de cursuri, lecții și exerciții personalizate. Utilizatorii pot să se înregistreze, să se autentifice și să își urmeze progresul în timp real.

Tehnologii și Dependențe

Aplicația utilizează următoarele tehnologii și librării:

- Lombok - Pentru a reduce boilerplate-ul codului Java.
- Spring Web - Framework pentru dezvoltarea aplicațiilor web.
- Spring Security - Implementarea securității aplicației.
- JDBC API - Conectivitate cu baza de date.
- Spring Data JPA - Gestionarea datelor persistente.
- MySQL Driver - Conexiunea cu baza de date MySQL.
- Validation - Validarea datelor de intrare.
- Spring Boot Actuator - Monitorizarea și managementul aplicației.
- CycloneDX SBOM - Suport pentru generarea SBOM (Software Bill of Materials).

Funcționalități API

1. Înregistrare Utilizator

Endpoint: /auth/register
Metodă: POST

Exemplu Request:

{
  "firstName": "Alice",
  "lastName": "Johnson",
  "email": "alice.johnson@example.com",
  "password": "LearnLanguage123"
}

2. Validare Token

Endpoint: /auth/validate-token
Metodă: GET

Exemplu Request:

token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

3. Autentificare Utilizator

Endpoint: /auth/login
Metodă: POST

Exemplu Request:

{
  "email": "alice.johnson@example.com",
  "password": "LearnLanguage123"
}

4. Obținere Informații Utilizator

Endpoint: /auth/me
Metodă: GET

Note: Nu este nevoie de input suplimentar; se bazează pe token-ul de autentificare.

5. Creare Curs

Endpoint: /courses/create-course
Metodă: POST

Exemplu Request:

{
  "code": "SPAN101",
  "title": "Introduction to Spanish",
  "description": "A beginner course on Spanish language basics.",
  "language": "Spanish"
}

6. Obținere Lista Cursuri

Endpoint: /courses/get-courses
Metodă: GET

Note: Nu este nevoie de input suplimentar.

7. Înscriere Utilizator la un Curs

Endpoint: /courses/{courseId}/enroll/{userId}
Metodă: POST

Exemplu Request:

courseId=1
userId=1

8. Obținere Cursuri ale Utilizatorului

Endpoint: /courses/user/{userId}
Metodă: GET

Exemplu Request:

userId=1

9. Creare Lecție

Endpoint: /lessons/create-lessons
Metodă: POST

Exemplu Request:

{
  "code": "L1",
  "title": "Basic Spanish Greetings",
  "description": "Learn how to greet people in Spanish.",
  "courseId": 1
}

10. Obținere Lecții pentru un Curs

Endpoint: /lessons/course/{courseId}
Metodă: GET

Exemplu Request:

courseId=1

11. Creare Exercițiu

Endpoint: /exercises/create-exercises
Metodă: POST

Exemplu Request:

{
  "question": "How do you say 'Hello' in Spanish?",
  "answer": "Hola",
  "lessonId": 1
}

12. Obținere Exerciții pentru o Lecție

Endpoint: /exercises/lesson/{lessonId}
Metodă: GET

Exemplu Request:

lessonId=1
