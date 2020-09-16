export interface IBanner {
  id?: number;
  title?: string;
  content?: string;
  frequency?: number;
}

export class Banner implements IBanner {
  constructor(public id?: number, public title?: string, public content?: string, public frequency?: number) {}
}
