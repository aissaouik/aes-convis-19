export interface IChoice {
  id?: number;
  choice?: string;
  questionId?: number;
}

export class Choice implements IChoice {
  constructor(public id?: number, public choice?: string, public questionId?: number) {}
}
