import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBanner } from 'app/shared/model/banner.model';

@Component({
  selector: 'aes-banner-detail',
  templateUrl: './banner-detail.component.html',
})
export class BannerDetailComponent implements OnInit {
  banner: IBanner | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ banner }) => (this.banner = banner));
  }

  previousState(): void {
    window.history.back();
  }
}
