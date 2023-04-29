import { HtmlParser } from '@angular/compiler';
import { Component } from '@angular/core';
import Konva from 'konva';
import { Layer } from 'konva/lib/Layer';
import { Stage } from 'konva/lib/Stage';
import {State} from "../../Interfaces/state";
import {HttpClient} from "@angular/common/http";
import {ServerCallerService} from "../../Services/server-caller.service";

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent {
  // ------------- Separator -------------
  private serverCaller: ServerCallerService;
  private myStage!: Stage;
  private board!: Layer;
  private rhSelected!: Boolean;
  private currentNodeNumber !: number;
  private edgesList: number[][] = [];
  private upDown: Boolean = true;
  private routhArray: number[] = [];
  private undoStates: State[] = [];
  private redoStates: State[] = [];
  private currentState!: State;
  // private currentState: {
  //   edgesList: number[][],
  //   board: Layer,
  //   rhSelected: Boolean,
  //   currentNodeNumber: number,
  //   upDown: Boolean,
  //   routArray: number[],
  // };
  /*
    [
    [ [to, weight], [to, weight], ..],
    [ [to, weight], [to, weight], ..],
    [ [to, weight], [to, weight], ..],
    [ [to, weight], [to, weight], ..],
    [ [to, weight], [to, weight]], ..,
   ]
   */
  // ------------- Separator -------------
  constructor(private http: HttpClient){
    this.serverCaller = new ServerCallerService(this.http);
  }
  ngOnInit(){
    this.rhSelected = false;
    this.switchSection(false);
    var stageHolder = document.getElementById('holder') as HTMLDivElement;
    if (stageHolder != null) {
      this.myStage = new Konva.Stage({
        width: stageHolder?.clientWidth - 10,
        height: stageHolder?.clientHeight,
        container: 'holder',
      });
    }
    this.board = new Konva.Layer();
    this.myStage.add(this.board);
    this.currentNodeNumber = 0;
    this.edgesList = [];
    this.routhArray = [];
    this.upDown = true;
    this.currentState = {
      myStage: this.myStage,
      edgesList: this.edgesList,
      rhSelected: this.rhSelected,
      currentNodeNumber: this.currentNodeNumber,
      upDown: this.upDown,
      routhArray: this.routhArray,
    }
    this.undoStates = [];
    this.redoStates = [];
    this.updateState();
  }
  // ------------- Separator -------------
  updateState(){
    this.undoStates.push(this.currentState);
    this.currentState = {
      myStage: this.myStage.toObject(),
      // edgesList: this.edgesList,
      edgesList: this.edgesList.map((x) => x.map((y) => y)),
      rhSelected: this.rhSelected,
      currentNodeNumber: this.currentNodeNumber,
      upDown: this.upDown,
      routhArray: this.routhArray,
    }
    this.redoStates = [];
    console.log(this.undoStates);
  }
  // ------------- Separator -------------
  updateStateFromObj(newState: State){
    this.myStage = Konva.Node.create(newState.myStage, 'holder');
    if(this.myStage.children != undefined)
      this.board = this.myStage.children[0];
    // this.edgesList = newState.edgesList;
    this.edgesList = newState.edgesList.map((x) => x.map((y) => y));
    this.rhSelected = newState.rhSelected;
    this.currentNodeNumber = newState.currentNodeNumber;
    this.upDown = newState.upDown;
    this.routhArray = newState.routhArray
  }
  // ------------- Separator -------------
  undoState(){
    if(this.undoStates.length == 1)
      return;
    this.redoStates.push(this.currentState);
    let tempState = this.undoStates.pop();
    if(tempState != undefined)
      this.currentState = tempState;
    this.updateStateFromObj(this.currentState);
    console.log(this.undoStates);
  }
  // ------------- Separator -------------
  redoState(){
    if(this.redoStates.length == 0)
      return;
    this.undoStates.push(this.currentState);
    let tempState = this.redoStates.pop();
    if(tempState != undefined)
      this.currentState = tempState;
    this.updateStateFromObj(this.currentState);
  }
  // ------------- Separator -------------
  clear(){
    this.ngOnInit();
  }
  // ------------- Separator -------------
  switchSection(rhSelected: Boolean){
    this.rhSelected = rhSelected;
    var toolbarSection = document.getElementById('toolbar') as HTMLDivElement;
    var rhSection = document.getElementById('rh') as HTMLDivElement;
    var konvaHolder = document.getElementById('holder') as HTMLDivElement;
    var rhHolder = document.getElementById('rhHolder') as HTMLDivElement;
    if(this.rhSelected){
      toolbarSection.style.display = 'none';
      rhSection.style.display = 'block';
      konvaHolder.style.display = 'none';
      rhHolder.style.display = 'block';
    } else {
      toolbarSection.style.display = 'block';
      rhSection.style.display = 'none';
      konvaHolder.style.display = 'block';
      rhHolder.style.display = 'none';
    }
  }
  // ------------- Separator -------------
  createNode(){
    let id = this.currentNodeNumber;
    this.currentNodeNumber++;
    var circle = new Konva.Group({
      x: 30,
      y: 30,
      width: 15,
      height: 15,
      draggable: true,
    });
    var c = new Konva.Circle({
      fill: 'gray',
      radius: 15,
      stroke: 'black',
      strokeWidth: 2,
    });
    circle.add(c);
    circle.add(
      new Konva.Text({
        text: String(id),
        fontSize: 18,
        x: -5,
        y: -7.5,
        fill: '#000',
        align: 'center',
      })
    );
    circle.addEventListener('dragend', () => {
      circle.setDraggable(false);
      this.updateState();
    })
    this.board.add(circle);
    // this.updateState();
  }
  // ------------- Separator -------------
  initEdge(){
    let x1 = 0;
    let y1 = 0;
    let x2 = 0;
    let y2 = 0;
    let i = 0;
    let start: string;
    let thisExtender = this;
    this.myStage.on('click', async function (e) {
      if (i == 0) {
        let object = e.target;
        start = object.getParent().getAttr('Children')[1].getAttr('text');
        console.log(start);
        object.getParent().setAttr('draggable', false);
        x1 = thisExtender.myStage.getPointerPosition()!.x;
        y1 = thisExtender.myStage.getPointerPosition()!.y;
        i++;
      } else if (i == 1) {
        let object = e.target;
        let terminal = object
          .getParent()
          .getAttr('Children')[1]
          .getAttr('text');
        console.log(terminal);
        object.getParent().setAttr('draggable', false);
        x2 = thisExtender.myStage.getPointerPosition()!.x;
        y2 = thisExtender.myStage.getPointerPosition()!.y;
        i++;
        let isCurved = true;
        if(Number(terminal) - Number(start) === 1){
          isCurved = false;
        }
        thisExtender.createEdge(x1, y1, x2, y2, thisExtender.addEdge(start, terminal, '1'), isCurved);
      }
    });
  }
  // ------------- Separator -------------
  createEdge(x1: number, y1: number, x2: number, y2: number, index: number, isCurved: Boolean){
    let thisExtender = this;
    const a = new Konva.Group({
      x: 0,
      y: 0,
    });
    let x3 = x1, y3 = y1;
    if(isCurved){
      if(this.upDown){
        y3 = y3 + (x2-x1)/3;
      }
      else {
        y3 = y3 - (x2-x1)/3;
      }
      x3 = x3 + (x2-x1)/2;
      this.upDown = !this.upDown;
    }
    a.add(
      new Konva.Arrow({
        points: [x1, y1, x3, y3, x2, y2],
        pointerLength: 10,
        pointerWidth: 10,
        fill: 'black',
        stroke: 'black',
        strokeWidth: 2,
        lineCap: 'round',
        tension: 0.5,
      })
    )
    var textNode = new Konva.Text({
      text: '1',
      x: x1 + (x2 - x1) / 2,
      y: y3,
      fontSize: 20,
    });
    textNode.on('dblclick dbltap', () => {
      // create textarea over canvas with absolute position

      // first we need to find position for textarea
      // how to find it?

      // at first lets find position of text node relative to the stage:
      var textPosition = textNode.getAbsolutePosition();

      // then lets find position of stage container on the page:
      var stageBox = this.myStage.container().getBoundingClientRect();

      // so position of textarea will be the sum of positions above:
      var areaPosition = {
        x: stageBox.left + textPosition.x,
        y: stageBox.top + textPosition.y,
      };

      // create textarea and style it
      var textarea = document.createElement('input');
      document.body.appendChild(textarea);

      textarea.value = textNode.text();
      textarea.style.position = 'absolute';
      textarea.style.top = areaPosition.y + 'px';
      textarea.style.left = areaPosition.x + 'px';
      textarea.style.width = '50px';
      textarea.style.height = '30px';
      textarea.style.padding = '5px';
      textarea.type = 'number';
      textarea.focus();

      textarea.addEventListener('mouseleave', function (e) {
          textNode.text(textarea.value);
          document.body.removeChild(textarea);
          thisExtender.edgesList[index][2] = Number(textarea.value);
          console.log(thisExtender.edgesList);
          thisExtender.updateState();
      });
    });

    a.add(textNode);
    this.board.add(a);
    // this.updateState();
  }
  // ------------- Separator -------------
  addEdge(from: string, to: string, weight: string){
    let from_ = Number(from);
    let to_ = Number(to);
    let weight_ = Number(weight);
    this.edgesList.push([from_, to_, weight_]);
    console.log(this.edgesList);
    return this.edgesList.length - 1;
  }
  // ------------- Separator -------------
  generateOrder(){
    var orderValue = document.getElementById('orderValue') as HTMLInputElement;
    var value = Number(orderValue.value);
    if(value < 0){
      alert('Enter positive number');
    }
    else {
      let orderGenerated = document.getElementById('orderGenerated') as HTMLDivElement;
      orderGenerated.innerHTML = "";
      for(let i=value;i>=0;i--){

        let orderDiv = document.createElement('div');
        let coeffInput = document.createElement('input');
        coeffInput.type = 'number';
        coeffInput.value = '0';
        let orderText = document.createElement('p');
        orderText.innerHTML = `S ` + `<sup>${this.numberToASCIICode(i)}</sup>`;

        orderDiv.style.display = 'flex';
        orderDiv.style.alignItems = 'center';
        orderDiv.style.padding = '2px';
        orderDiv.style.justifyContent = 'space-between';

        coeffInput.style.width = '70px';
        coeffInput.style.marginRight = '5px';
        coeffInput.style.outline = 'none';
        coeffInput.style.padding = '5px';
        coeffInput.style.fontSize = '15px';

        orderText.style.padding = '2px';

        if(i != 0){
          orderText.appendChild(document.createTextNode("      +"));
        }
        else {
          orderText.innerHTML = "";
        }

        orderDiv.appendChild(coeffInput);
        orderDiv.appendChild(orderText);
        orderGenerated.appendChild(orderDiv);
      }
    }
  }

  numberToASCIICode(num: Number) {
    // Convert the number to a string
    let str = num.toString();
    let result = '';

    // Loop through each character in the string
    for (let i = 0; i < str.length; i++) {
      // Get the ASCII code for the current character
      let code = str.charCodeAt(i);
      // Add the ASCII code to the result string
      result += `&#${code};`;
    }

    // Return the result string
    return result;
  }

  // ------------- Separator ------------
  solveRouth(){
    let orderGenerated = document.getElementById('orderGenerated') as HTMLDivElement;
    this.routhArray = [];
    for(let i=0;i<orderGenerated.children.length;i++){
      this.routhArray.push(Number(((orderGenerated.children[i].children[0]) as HTMLInputElement).value));
    }
    console.log(this.routhArray);
    let result = this.serverCaller.getRouth(this.routhArray);
  }
  // ------------- Separator ------------
  async solveSFG(){
    console.log(this.edgesList);
    console.log(`System initialized = ` + await this.serverCaller.init_system(this.edgesList, this.currentNodeNumber))
    let FPS = await this.serverCaller.getFPs();
    let loops = await this.serverCaller.getLoops();
    let nonTouchingLoops = await this.serverCaller.getNonTouchingLoops();
    let deltas = await this.serverCaller.getDeltas();
    let transferFunction = this.serverCaller.getTf();
  
  }
}
