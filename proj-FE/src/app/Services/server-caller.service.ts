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
    console.log("Initializing system with:")
    console.log(edgeList);
    return await firstValueFrom(
      this.http.post<boolean>(this.url + `init/${numberOfNodes}`, edgeList)
    )
  }

  async getFPs() {
    return await firstValueFrom(
      this.http.post<any[][]>(this.url + 'fps', null)
    );
  }

  async getLoops() {
    let temp = await firstValueFrom(
      this.http.post<any[]>(this.url + 'loops', null)
    );
    for(let i = 0 ; i < temp.length ; i++){
      temp[i] = Object.values(temp[i])
    }
    console.log('Heree')
    console.log(temp)
    return temp
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

  async getRouth(routhArray: number[]) {
    return await firstValueFrom(
      this.http.post<number>(this.url + 'routh', routhArray)
    );
  }


}
