package io.github.kusaanko.Shooting;

public class SceneMng {
	EnumShootingScreen screen = EnumShootingScreen.MENU;

public void SceneUddate() {
	switch(screen) {
	case MENU:
		break;
	case OPTION:
		break;
	case GAME:
		break;
	case GAME_OVER:
		break;
	}
}

public void ScreenDraw() {
	switch(screen){      
    case MENU:
    	MENU.MENU_draw();
        break;
    case GAME:
        break;
    case OPTION:
        break;
    case GAME_OVER:
    	break;
    }
}
}
