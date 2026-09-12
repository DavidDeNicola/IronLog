import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from '../sidebar/sidebar.component';
import {TabBarComponent} from '../tab-bar/tab-bar.component';

@Component({
  selector: 'app-main-layout',
  imports: [RouterOutlet, SidebarComponent, TabBarComponent],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.scss'
})
export class MainLayoutComponent {

  sidebarAperta = false;

  apriSidebar(): void {
    this.sidebarAperta = true;
  }

  chiudiSidebar(): void {
    this.sidebarAperta = false;
  }
}
