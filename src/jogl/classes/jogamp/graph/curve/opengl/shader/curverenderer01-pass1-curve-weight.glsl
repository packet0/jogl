
    if( gcv_TexCoord.x == 0.0 && gcv_TexCoord.y == 0.0 ) {
        // pass-1: Lines
        color = gcu_ColorStatic.rgb;
        alpha = 1.0;
    } else {
        // pass-1: curves
        const vec2 rtex = vec2( abs(gcv_TexCoord.x), abs(gcv_TexCoord.y) - 0.1 );

        const vec2 dtx = dFdx(rtex);
        const vec2 dty = dFdy(rtex);
          
        const float w = gcu_Weight;
        const float pd = ((2.0 - (2.0*w))*rtex.x*rtex.x) + 2.0*(w-1.0)*rtex.x + 1.0;
        const float position = rtex.y - ((w*rtex.x*(1.0 - rtex.x))/pd);

        const float aph = 2.0 - 2.0*w;
        
        const float gd = (aph*rtex.x*rtex.x + 2.0*rtex.x + 1.0)*(aph*rtex.x*rtex.x + 2.0*rtex.x + 1.0);
        const vec2 f = vec2((dtx.y - (w*dtx.x*(1.0 - 2.0*rtex.x))/gd), (dty.y - (w*dty.x*(1.0 - 2.0*rtex.x))/gd));

        color = gcu_ColorStatic.rgb;
        alpha = clamp(0.5 - ( position/length(f) ) * sign(gcv_TexCoord.y), 0.0, 1.0);
    }
