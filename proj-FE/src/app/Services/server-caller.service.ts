import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {firstValueFrom} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ServerCallerService {
  private port = 8080;
  private url = `http://localhost:${this.port}/server/`;
  constructor(private http: HttpClient) { }

  async getFPs() {
    return await firstValueFrom(
      this.http.post<any[][]>(this.url + 'fps', null)
    );
  }

  async getLoops() {
    return await firstValueFrom(
      this.http.post<any[][]>(this.url + 'loops', null)
    );
  }

  async getNonTouchingLoops() {
    return await firstValueFrom(
      this.http.post<any[][][]>(this.url + 'ntLoops', null)
    );
  }

  async getDeltas() {
    return await firstValueFrom(
      this.http.post<number[]>(this.url + 'ntLoops', null)
    );
  }

  async getTf() {
    return await firstValueFrom(
      this.http.post<number>(this.url + 'tf', null)
    );
  }

  async getRouth() {
    return await firstValueFrom(
      this.http.post<number>(this.url + 'routh', null)
    );
  }


}