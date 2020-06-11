import { Injectable } from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router} from '@angular/router';
import { Observable } from 'rxjs';
import {AuthService} from './auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class RouteGuard implements CanActivate {

  constructor(private auth: AuthService, private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.isLogged(state.url);    // controllo se l'utente è loggato oppure no
  }

  isLogged(url: string){
    if (!this.auth.getUser()){
      this.auth.setUrl(url);          // tengo traccia dell'ultimo url dell'utente per ridirigerlo in seguito alla login
      this.router.navigate(['home'], {queryParams: {doLogin: 'true'}});
      return false;
    }
    return true;
  }

}
