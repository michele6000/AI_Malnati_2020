<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--direttiva per l'ambiente di traduzione che dice di trasformare la jsp in java(?)--%>
<%-- diventeranno il grosso di quello che stiamo facendo--%>
<html>
<head>
    <title>Title</title>
    <style>
        body, html {
            font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif;
            background-color: beige;
            padding: 2em;
        }
        .counter{
            background-color: burlywood;
            padding: 0.5em;
            color: black;
            font-size: 1.5em;
        }
    </style>
</head>
<body>
<h1>Esempio di JSP</h1>
<%-- Predo il mutex associato alla sessione (ogni oggetto in java possiede un mutex),
 ne faccio il lock, tutto ciò che avviene all'interno può essere eseguito
un solo thread per volta. Sessioni che fanno riferimento allo stesso cookie vanno eseguite una per volta--%>
<%
     Integer counter;
    synchronized (session) {
        counter = (Integer) session.getAttribute("counter");
        if (counter == null) {
            session.setAttribute("counter", 1);
            counter = 1;
        } else {
            counter++;
            session.setAttribute("counter", counter);
        }
        if (counter == 9)
            session.invalidate();
    }
%>
<p>Il valore attuale del contatore è: <span class="counter"> <%= counter%> </span></p>

</body>
</html>

<%--Invalidare una sessione vuol dire che butto via tutta la mappa, ossia elimino il cookie della sessione.
Se l'utente ricarica dopo 9 il counter torna a 1.
La sessione quindi è un modo per tenere traccia delle informazioni che restano.--%>