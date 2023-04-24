import {Layer} from "konva/lib/Layer";
import {Stage} from "konva/lib/Stage";

export interface State {
  myStage: Object,
  edgesList: number[][],
  rhSelected: Boolean,
  currentNodeNumber: number,
  upDown: Boolean,
  routhArray: number[],
}
