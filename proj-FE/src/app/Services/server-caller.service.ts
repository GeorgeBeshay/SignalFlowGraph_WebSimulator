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

  async init_system(edgeList: number[][], numberOfNodes: number){
    console.log(edgeList[0]);
    return await firstValueFrom(
      this.http.post<boolean>(this.url + `init/${numberOfNodes}`, [1.5, 2, 3, 4])
    )
  }

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
