import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChoice } from 'app/shared/model/choice.model';

@Component({
  selector: 'aes-choice-detail',
  templateUrl: './choice-detail.component.html',
})
export class ChoiceDetailComponent implements OnInit {
  choice: IChoice | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ choice }) => (this.choice = choice));
  }

  previousState(): void {
    window.history.back();
  }
}
