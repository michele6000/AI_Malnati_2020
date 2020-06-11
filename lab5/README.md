# Lab5

Studente: Paola Caso - s257009

## Comandi (da eseguire nella directory principale)

1. Run `npm install`
2. Run `json-server-auth db.json`
3. Run `npm start`

La pagina è ora disponibile alla URL: [http://localhost:4200](http://localhost:4200)

## Struttura dati (database)

- Tabella studenti del corso di Applicazioni Internet, con `courseId = 1`:

        "students": 
            {
            "id": "s253309",
            "name": "Burlacu",
            "firstName": "Iustin",
            "group": "",
            "courseId": 1
            },
            {
             "id": "s236564",
             "name": "Lamberti",
             "firstName": "Giovanni",
             "group": "",
             "courseId": 1
            },
            {
             "id": "s257009",
             "name": "Caso",
             "firstName": "Paola",
             "group": "",
             "courseId": 1
            },
            {
             "id": "s238906",
             "name": "Bruno",
             "firstName": "Alberto",
             "group": "",
             "courseId": 1
            }
   
    
  Nel database sono anche presenti altri studenti, visibili nel menù a tendina che si apre nel form (`Add student`) 
  con autocomplete, quando si comincia a digitare qualcosa. 
  
- È presente, inoltre, il gruppo d'esempio `Calvary` con `id = 1`.

        "groups": 
            {
              "id": 1,
              "name": "Calvary"
            }

- L'unico corso disponibile e attualmente gestito è quello di `Applicazioni Internet`.

        "courses": 
         {
          "id": 1,
          "name": "Applicazioni Internet",
          "path": "applicazioni-internet"
         }
         
- L'unico `user` registrato con cui è possibile loggarsi è: `olivier@mail.com` con password "bestPassw0rd".


## Gestione delle route

  All'interno del file `app-routing.module.ts` sono configurate del route e le viste del sito web.

        const routes: Routes = [
          // Home Page
          {path: 'home', component: HomeComponent},
          // Course/Students Page
          {path: 'teacher/course/applicazioni-internet/students', component: StudentsContComponent, canActivate: [RouteGuard]},
          // Vms Page
          {path: 'teacher/course/applicazioni-internet/vms', component: VmsContComponent, canActivate: [RouteGuard]},
          // Page not found view
          {path: '**', component: PageNotFoundComponent}
        ];
        
  La `Home Page` appare di default non appena viene aperto il sito web, oppure cliccando sul titolo in alto a sinistra 
  (`VirtualLabs`).
  Se si è loggati, cliccando sulla `<tab>` relativa al corso di `Applicazioni Internet` oppure su quella `Students`, è possibile
  vedere la tabella con gli studenti iscritti. È possibile, inoltre, aggiungerne altri o eliminarli dal corso.
  Cliccando su `Vms` viene aperta la vista del componente corrispondente, mentre per tutto ciò che non è ancora implementato
  si viene reindirizzati alla vista di `Page not found` contenente un generico messaggio di errore.
  Se invece non si è effettuata la `Login`, al click delle varie `<tab>` viene aperta la finestra di dialogo per poter accedere
  al sito e quindi alle viste sopracitate.
  
  
    

