//        for (int i = 0; i <= 100000; i++) {
//            Dimension size = getSize();
//            int w = size.width ;
//            int h = size.height;
//
//            Random r = new Random();
//            int x = Math.abs(r.nextInt()) % w;
//            int y = Math.abs(r.nextInt()) % h;
//            g2d.drawLine(x, y, x, y);
//        }
//
//        int step = 10;
//        Random r = new Random();
//        int[] ground = new int[1000 / step];
//        ground[0] = 500;
//        for (int i = 1; i < ground.length; i++) {
//            ground[i] = ground[i - 1] + (Math.abs(r.nextInt()) % 10) - 5;
//        }
//        g2d.setStroke(new BasicStroke(3));
//        for (int x = 0; x < ground.length - 1; x++) {
//            g2d.drawLine(step * x, ground[x], step * (x + 1), ground[x + 1]);
//        }