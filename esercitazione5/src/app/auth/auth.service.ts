import { EventEmitter, Injectable, Output } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserModule } from '../user.module';
import { BehaviorSubject, Observable } from 'rxjs';

// const API_URL_LOGIN = 'http://localhost:3000/login';
// const API_URL_LOGIN = 'http://localhost:4200/api/login';    // http con proxy
const API_URL_LOGIN = 'https://localhost:4200/api/login';      // https con proxy

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  // tslint:disable-next-line:no-output-rename
  @Output('userLogged') userLogged = new EventEmitter();
  user: Observable<UserModule>;
  private userSubject: BehaviorSubject<UserModule>;

  constructor(private http: HttpClient) {
    const user = new UserModule();
    if (localStorage.getItem('token')) {
      user.isLogged = true;
      user.email = localStorage.getItem('email');
      user.accessToken = localStorage.getItem('token');
    }
    this.userSubject = new BehaviorSubject<UserModule>(user);
    this.user = this.userSubject.asObservable();
  }

  login(email: string, password: string){
    this.http.post(
      API_URL_LOGIN, {
        email,
        password
      }
    ).subscribe(
      (payload: any) => {
        localStorage.setItem('email', email);
        localStorage.setItem('token', payload.accessToken);
        const user: UserModule = new UserModule();
        user.email = email;
        user.accessToken = payload.accessToken;
        user.isLogged = true;
        this.userSubject.next(user);
        this.userLogged.emit(true);
      },
      (error: any) => {
        this.userLogged.emit(false);    // propago l'evento se non è andato a buon fine la login -> in login.ts lo ascolto nel costruttore
      }
    );

  }

  logout(){
    localStorage.removeItem('email');
    localStorage.removeItem('token');
    this.userSubject.next(null);      // svuoto l'observable
  }

  getUser(){
    return localStorage.getItem('email');

  }

  getToken(){
    const token = localStorage.getItem('token');
    if (!token) {
      return '';
    }
    return token;
  }

  setUrl(url: string){
    localStorage.setItem('url', url);
  }

  getUrl(){
    return localStorage.getItem('url');
  }

  deleteUrl(){
    localStorage.removeItem('url');
  }

}
