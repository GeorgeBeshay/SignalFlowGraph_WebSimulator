import { Component } from '@angular/core';
import Konva from 'konva';
import { Layer } from 'konva/lib/Layer';
import { Stage } from 'konva/lib/Stage';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent {
  // ------------- Separator -------------
  private myStage!: Stage;
  private board!: Layer;
  private rhSelected!: Boolean;
  private currentNodeNumber !: number;
  private edgesList: number[][] = [];
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
  constructor(){
    
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
  }
  // ------------- Separator -------------
  switchSection(rhSelected: Boolean){
    this.rhSelected = rhSelected;
    var toolbarSection = document.getElementById('toolbar') as HTMLDivElement;
    var rhSection = document.getElementById('rh') as HTMLDivElement;
    if(this.rhSelected){
      toolbarSection.style.display = 'none';
      rhSection.style.display = 'block';
    } else {
      toolbarSection.style.display = 'block';
      rhSection.style.display = 'none';
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
    })
    this.board.add(circle);
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
        thisExtender.createEdge(x1, y1, x2, y2, thisExtender.addEdge(start, terminal, '1'));
      }
    });
  }
  // ------------- Separator -------------
  createEdge(x1: number, y1: number, x2: number, y2: number, index: number){
    let thisExtender = this;
    const a = new Konva.Group({
      x: 0,
      y: 0,
    });
    a.add(
      new Konva.Arrow({
        points: [x1, y1, x2, y2],
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
      y: y1 + (y2 - y1) / 2,
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
      });
    });

    a.add(textNode);
    this.board.add(a);
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
}
