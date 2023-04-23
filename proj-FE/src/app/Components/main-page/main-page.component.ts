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

  }
  // ------------- Separator -------------
  createEdge(){
    const a = new Konva.Group({
      x: 50,
      y: 50,
      draggable: true
    });
    a.add(
      new Konva.Arrow({
        points: [50, 50, 200, 200],
        fill: 'black',
        stroke: 'black',
        strokeWidth: 2,
      })
    )
    a.add(
      new Konva.Text({
        text: "test",
        fontSize: 18,
        fontFamily: 'Calibri',
        fill: '#000',
        width: 70,
        padding: 10,
        align: 'center',
      })
    );
    this.board.add(a);
  }
  // ------------- Separator -------------

  // ------------- Separator -------------
}
