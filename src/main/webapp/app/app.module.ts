import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { Covid19SharedModule } from 'app/shared/shared.module';
import { Covid19CoreModule } from 'app/core/core.module';
import { Covid19AppRoutingModule } from './app-routing.module';
import { Covid19HomeModule } from './home/home.module';
import { Covid19EntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    Covid19SharedModule,
    Covid19CoreModule,
    Covid19HomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    Covid19EntityModule,
    Covid19AppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent],
})
export class Covid19AppModule {}
